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
import { Input } from '@/components/ui/input';
import Link from 'next/link';
import { FINANZAS_ACTUALIZAR_COSTO, FINANZAS_API } from '@/constants';
import { useRouter, useSearchParams } from 'next/navigation';

import { useToast } from '@/hooks/use-toast';
import { useRoles } from '../context/RolesContext';
import { useEffect } from 'react';

const formSchema = z.object({
  sueldo: z.coerce.number().min(1, {
    message: 'El sueldo no puede ser menor a 1',
  }),
});

export default function ClientComponent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const id = searchParams.get('id');
  const toast = useToast();

  const { data, editSingleCosto } = useRoles();

  const costoBuscado = data.find((costo) => costo.id.toString() === id);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      sueldo: costoBuscado?.costo || 0,
    },
  });

  useEffect(() => {
    form.reset({
      sueldo: costoBuscado?.costo,
    });
  }, [costoBuscado]);

  const onSubmit = async (data: z.infer<typeof formSchema>) => {
    console.log('SUBMIT', data);
    const res = await fetch(FINANZAS_API + FINANZAS_ACTUALIZAR_COSTO + costoBuscado?.id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        costo: data.sueldo,
      }),
    });

    if (!res.ok || !costoBuscado) {
      toast.toast({
        title: 'Error',
        description: 'Ocurrió un error al cargar el costo',
        variant: 'destructive',
      });

      return;
    }

    editSingleCosto({ ...costoBuscado, costo: data.sueldo });

    toast.toast({
      title: 'Costo actualizado',
      description: 'El costo fue actualizado correctamente',
      variant: 'success',
    });

    router.push('/costos-rol');
  };

  return (
    <div className="mb-8">
      <h1 className="text-center text-4xl font-bold mt-8 mb-16">Cargar costo mensual</h1>

      <Card className="w-[500px] mx-auto">
        <CardHeader>
          <CardTitle>{id ? 'Actualizar un costo mensual' : 'Cargar costo mensual'}</CardTitle>
          <CardDescription>
            Seleccione el nuevo costo para el rol y la experiencia seleccionada.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form className="space-y-8" onSubmit={form.handleSubmit(onSubmit)}>
              <div className="border-slate-200 border rounded p-4">
                <p className="text-lg font-bold mb-4">Información del recurso seleccionado</p>
                <div className="flex flex-col gap-2 font-semibold">
                  <p>
                    Fecha:{' '}
                    <span className="font-normal">
                      {costoBuscado?.mes + '/' + costoBuscado?.anio}
                    </span>
                  </p>
                  <p>
                    Rol: <span className="font-normal">{costoBuscado?.rol.nombre}</span>
                  </p>
                  <p>
                    Experiencia:{' '}
                    <span className="font-normal">{costoBuscado?.rol.experiencia}</span>
                  </p>
                </div>
              </div>
              <FormField
                control={form.control}
                name="sueldo"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Costo</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="Selecciona un sueldo en divisa dolar"
                        {...field}
                        type="number"
                        onInput={(e) => {
                          e.currentTarget.value = e.currentTarget.value.replace(/[^0-9.]/g, '');
                        }}
                        min={0}
                      />
                    </FormControl>
                    <FormDescription>Ingrese el nuevo costo a actualizar</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <div className="flex justify-between w-full items-center">
                <Link href={'/costos-rol'}>
                  <Button variant={'ghost'} className="font-semibold">
                    <ArrowLeft className="h-4 w-4 shrink-0" />
                    <p>Volver</p>
                  </Button>
                </Link>

                <Button type="submit" className="bg-primary font-semibold">
                  {id ? 'Actualizar costo' : 'Cargar costo'}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
