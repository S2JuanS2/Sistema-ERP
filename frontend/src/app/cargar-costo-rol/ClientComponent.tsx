'use client';

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '@/components/ui/button';
import { ArrowLeft } from 'lucide-react';
import { rolesPosibles } from './page';
import DatePicker from 'react-datepicker';
import { DateInput } from '@/components/DateInput';
import { Input } from '@/components/ui/input';
import Link from 'next/link';
import { useRouter, useSearchParams } from 'next/navigation';

import { registerLocale } from 'react-datepicker';
import { es } from 'date-fns/locale';
import { useRoles } from '../context/RolesContext';
import { costos } from '@/types/costos';
import { useEffect } from 'react';
import { useToast } from '@/hooks/use-toast';
import {
  FINANZAS_ACTUALIZAR_VARIOS_COSTOS,
  FINANZAS_API,
  FINANZAS_CARGAR_COSTO,
  MESES,
} from '@/constants';
import { Mes } from '@/types/enums';
registerLocale('es', es);

const formSchema = z.object({
  fecha: z.date({
    message: 'La fecha es requerida',
  }),
  roles: z.array(
    z.object({
      rol: z.string(),
      experiencia: z.array(
        z.object({
          nombre: z.string(),
          sueldo: z.coerce.number().min(1, {
            message: 'El sueldo no puede ser menor a 1',
          }),
        })
      ),
    })
  ),
});

type ClientComponentProps = {
  rolesPosibles: rolesPosibles[];
};

export default function ClientComponent({ rolesPosibles }: ClientComponentProps) {
  const searchParams = useSearchParams();
  const mes = Mes[searchParams.get('mes') as keyof typeof Mes];
  const anio = searchParams.get('anio');
  const editando = searchParams.get('editar') === 'true';

  const { data, addCostos, editCostos } = useRoles();
  const router = useRouter();
  const toast = useToast();

  const buscarCosto = (rol: string, experiencia: string) => {
    const costo = data.find(
      (costo: costos) =>
        costo.rol.nombre === rol &&
        costo.rol.experiencia === experiencia &&
        costo.mes === mes.toString() &&
        costo.anio === anio
    );
    return costo ? costo.costo : 0;
  };

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      fecha: anio && mes ? new Date(+anio, +mes - 1) : new Date(),
      roles: rolesPosibles.map((rol) => ({
        rol: rol.nombre,
        experiencia: rol.experiencia.map((experiencia) => ({
          nombre: experiencia,
          sueldo: editando ? buscarCosto(rol.nombre, experiencia) : 0,
        })),
      })),
    },
  });

  useEffect(() => {
    if (editando && data) {
      form.reset({
        fecha: anio && mes ? new Date(+anio, +mes - 1) : new Date(),
        roles: rolesPosibles.map((rol) => ({
          rol: rol.nombre,
          experiencia: rol.experiencia.map((experiencia) => ({
            nombre: experiencia,
            sueldo: buscarCosto(rol.nombre, experiencia),
          })),
        })),
      });
    }
  }, [data]);

  const onSubmit = async (formData: z.infer<typeof formSchema>) => {
    if (!editando) {
      const duplicated = data.find(
        (costo: costos) =>
          costo.mes === (formData.fecha.getMonth() + 1).toString() &&
          costo.anio === formData.fecha.getFullYear().toString()
      );

      if (duplicated) {
        toast.toast({
          title: 'Error al cargar el costo',
          description: `Ya existe un costo para el mes ${
            MESES[formData.fecha.getMonth() + 1]
          } del año ${formData.fecha.getFullYear()}`,
          variant: 'destructive',
        });
        return;
      }
    }

    if (editando) {
      // El put debe recibir algo del estilo: [ {id del costo}: { costo: {costo nuevo} }, {id del costo}: { costo: {costo nuevo} } ]
      const putData = data
        .filter(
          (costo: costos) => anio && costo.mes === mes.toString() && costo.anio === anio.toString()
        )
        .map((costo: costos) => ({
          [costo.id]: {
            costo:
              formData.roles
                .find((rol) => rol.rol === costo.rol.nombre)
                ?.experiencia.find((experiencia) => experiencia.nombre === costo.rol.experiencia)
                ?.sueldo ?? 0,
          },
        }));

      // Convert array to object array
      const putDataObject = Object.assign({}, ...putData.map((item) => item));

      const res = await fetch(FINANZAS_API + FINANZAS_ACTUALIZAR_VARIOS_COSTOS, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(putDataObject),
      });

      if (!res.ok) {
        toast.toast({
          title: 'Error al actualizar el costo',
          description: 'Hubo un error al actualizar el costo, por favor intente nuevamente.',
          variant: 'destructive',
        });
        return;
      }

      toast.toast({
        title: 'Se actualizó el costo correctamente',
        description: `Se actualizaron los costos correctamente para el mes ${
          MESES[formData.fecha.getMonth() + 1]
        } del año ${formData.fecha.getFullYear()}`,
        variant: 'success',
      });

      const putJson = await res.json();

      editCostos(putJson);
    } else {
      const postData = formData.roles.flatMap((rol) =>
        rol.experiencia.map((experiencia) => ({
          nombre: rol.rol,
          experiencia: experiencia.nombre,
          costo: experiencia.sueldo,
          mes: (formData.fecha.getMonth() + 1).toString(),
          anio: formData.fecha.getFullYear().toString(),
        }))
      );

      const res = await fetch(FINANZAS_API + FINANZAS_CARGAR_COSTO, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(postData),
      });

      if (!res.ok) {
        toast.toast({
          title: 'Error al cargar el costo',
          description: 'Hubo un error al cargar el costo, por favor intente nuevamente.',
          variant: 'destructive',
        });
        return;
      }

      const postJson = await res.json();
      toast.toast({
        title: `Se ${editando ? 'actualizó' : 'agregó'} el costo correctamente`,
        description: `Se ${
          editando ? 'actualizaron' : 'agregaron'
        } los costos correctamente para el mes ${
          MESES[formData.fecha.getMonth() + 1]
        } del año ${formData.fecha.getFullYear()}`,
        variant: 'success',
      });

      addCostos(postJson);
    }

    router.push('/costos-rol');
  };

  return (
    <div className="mb-8">
      <h1 className="text-center text-4xl font-bold mt-8 mb-16">Carga de costos mensuales</h1>
      <Card className="mx-auto w-fit">
        <CardHeader>
          <CardTitle>
            {editando ? 'Actualizar un costo mensual' : 'Cargar costos mensuales'}
          </CardTitle>
          <CardDescription>
            Seleccione una fecha y el rol para cargar el costo mensual.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form className="space-y-8" onSubmit={form.handleSubmit(onSubmit)}>
              <FormField
                control={form.control}
                name="fecha"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Periodo</FormLabel>
                    <FormControl>
                      <DatePicker
                        selected={field.value}
                        onChange={(date: Date | null) => {
                          if (date) {
                            form.setValue('fecha', date);
                          }
                        }}
                        calendarClassName="datePicker"
                        locale={'es'}
                        disabled={editando}
                        showMonthYearPicker
                        dateFormat="MM/yyyy"
                        minDate={new Date(2000, 0)}
                        maxDate={new Date()}
                        customInput={<DateInput disabled={editando} />}
                      />
                    </FormControl>
                    <FormDescription>Seleccione el mes y el año que desea cargar.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {rolesPosibles.map((rol, rolIndex) => (
                <div
                  key={rol.nombre}
                  className="flex flex-col border-gray-200 border p-4 rounded-lg w-fit"
                >
                  <div className="flex">
                    <p className="font-semibold">Rol:</p>
                    <span className="ml-2">{rol.nombre}</span>
                  </div>

                  <div className="grid grid-cols-3 gap-8 mt-4">
                    {rol.experiencia.map((experiencia, experienciaIndex) => (
                      <FormField
                        key={experiencia}
                        control={form.control}
                        name={`roles.${rolIndex}.experiencia.${experienciaIndex}.sueldo`}
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel className="font-semibold">{experiencia}</FormLabel>
                            <FormControl>
                              <Input
                                placeholder="Escribe un sueldo en dólares"
                                className="min-w-[250px]"
                                {...field}
                                type="number"
                                onInput={(e) => {
                                  e.currentTarget.value = e.currentTarget.value.replace(
                                    /[^0-9.]/g,
                                    ''
                                  );
                                }}
                                min={0}
                              />
                            </FormControl>

                            <FormMessage />
                          </FormItem>
                        )}
                      />
                    ))}
                  </div>
                  <FormDescription className="mt-4">
                    Seleccione un sueldo para cada experiencia
                  </FormDescription>
                </div>
              ))}

              <div className="flex justify-between w-full items-center">
                <Link href={'/costos-rol'}>
                  <Button variant={'ghost'} className="font-semibold">
                    <ArrowLeft className="h-4 w-4 shrink-0" />
                    <p>Volver</p>
                  </Button>
                </Link>

                <Button type="submit" className="bg-primary font-semibold">
                  {editando ? 'Actualizar costo' : 'Cargar costo'}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
