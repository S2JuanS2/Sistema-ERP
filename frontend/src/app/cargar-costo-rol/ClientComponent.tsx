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

import { registerLocale } from 'react-datepicker';
import { es } from 'date-fns/locale';
import { useRoles } from '../context/RolesContext';
import { costos } from '@/types/costos';
import { useEffect } from 'react';
import { useToast } from '@/hooks/use-toast';
import { MESES } from '@/constants';
registerLocale('es', es);

export default function ClientComponent({ rolesPosibles }: ClientComponentProps) {
  const searchParams = useSearchParams();
  const mes = searchParams.get('mes');
  const anio = searchParams.get('anio');
  const editar = mes && anio ? true : false;

  const { data, setData } = useRoles();
  const router = useRouter();
  const toast = useToast();

  const buscarCosto = (rol: string, experiencia: string) => {
    const costo = data.find(
      (costo: costos) =>
        costo.rol.nombre === rol &&
        costo.rol.experiencia === experiencia &&
        costo.mes === mes &&
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
          sueldo: editar ? buscarCosto(rol.nombre, experiencia) : 0,
        })),
      })),
    },
  });

  useEffect(() => {
    if (editar && data) {
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

  const onSubmit = async (newData: z.infer<typeof formSchema>) => {
    // Add it to the API
    // ...

    console.log('NUEVA DATA', newData);
    console.log('DATA', data);

    /*
     * Recordatorio: Cuaando haga el POST a la api, esta deberia devolverme los datos nuevos,
     * por lo que yo deberia poder actualizar la data sin necesidad
     * de hacer un GET a la API.
     */

    toast.toast({
      title: `Se ${editar ? 'actualizó' : 'agregó'} el costo correctamente`,
      description: `Se ${
        editar ? 'actualizaron' : 'agregaron'
      } los costos correctamente para el mes ${
        MESES[newData.fecha.getMonth() + 1]
      } del año ${newData.fecha.getFullYear()}`,
    });

    router.push('/costos-rol');
  };

  return (
    <div className="mb-8">
      <h1 className="text-center text-4xl font-bold mt-8 mb-16">Carga de costos mensuales</h1>

      <Card className="mx-auto w-fit">
        <CardHeader>
          <CardTitle>
            {editar ? 'Actualizar un costo mensual' : 'Cargar costos mensuales'}
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
                        locale={'es'}
                        disabled={!!mes && !!anio}
                        showMonthYearPicker
                        dateFormat="MM/yyyy"
                        minDate={new Date(2000, 0)}
                        maxDate={new Date()}
                        customInput={<DateInput disabled={!!mes && !!anio} />}
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
                  {editar ? 'Actualizar costo' : 'Cargar costo'}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
