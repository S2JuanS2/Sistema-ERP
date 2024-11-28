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
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { ArrowLeft, Check, ChevronsUpDown } from 'lucide-react';
import { cn } from '@/lib/utils';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { rolesPosibles } from './page';
import { useState } from 'react';
import DatePicker from 'react-datepicker';
import { DateInput } from '@/components/DateInput';
import { Input } from '@/components/ui/input';
import Link from 'next/link';
import { FINANZAS_ACTUALIZAR_COSTO, FINANZAS_API, FINANZAS_CARGAR_COSTO } from '@/constants';
import { useRouter, useSearchParams } from 'next/navigation';
import { costos } from '@/types/costos';

const formSchema = z.object({
  fecha: z.date({
    message: 'La fecha es requerida',
  }),
  sueldo: z.coerce.number().min(1, {
    message: 'El sueldo no puede ser menor a 1',
  }),
  rol: z.string().min(1, {
    message: 'El rol es requerido',
  }),
  seniority: z.string().min(1, {
    message: 'La experiencia es requerida',
  }),
});

type ClientComponentProps = {
  rolesPosibles: rolesPosibles[];
  registeredData: costos[];
};

import { registerLocale } from 'react-datepicker';
import { es } from 'date-fns/locale';
import { toast } from '@/hooks/use-toast';
registerLocale('es', es);

export default function ClientComponent({ rolesPosibles, registeredData }: ClientComponentProps) {
  const [selectedRole, setSelectedRole] = useState<string | null>(null);

  const router = useRouter();
  const searchParams = useSearchParams();
  const rol = searchParams.get('rol');
  const experiencia = searchParams.get('experiencia');
  const costo = Number(searchParams.get('costo'));
  const mes = searchParams.get('mes');
  const anio = searchParams.get('anio');
  const id = searchParams.get('id');

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      fecha: anio && mes ? new Date(+anio, +mes - 1) : new Date(),
      rol: rol || '',
      seniority: experiencia || '',
      sueldo: costo || 0,
    },
  });

  const onSubmit = async (data: z.infer<typeof formSchema>) => {
    if (id) {
      const res = await fetch(FINANZAS_API + FINANZAS_ACTUALIZAR_COSTO + id, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          costo: data.sueldo,
        }),
      });

      const jsonRes: costos = await res.json();

      if (res.ok) {
        router.push(
          '/costos-rol?id=' +
            jsonRes.id +
            '&nombre=' +
            data.rol +
            '&experiencia=' +
            data.seniority +
            '&costo=' +
            data.sueldo +
            '&editado=true'
        );
      } else {
        console.error('Error al actualizar el costo', res);
      }

      return;
    }

    const newCosto = {
      nombre: data.rol,
      experiencia: data.seniority,
      anio: data.fecha.getFullYear(),
      mes: data.fecha.getMonth() + 1,
      costo: data.sueldo,
    };

    const seachDuplicate = registeredData.find(
      (costo) =>
        costo.rol.nombre === newCosto.nombre &&
        costo.rol.experiencia === newCosto.experiencia &&
        costo.anio === newCosto.anio.toString() &&
        costo.mes === newCosto.mes.toString()
    );

    if (seachDuplicate) {
      toast({
        variant: 'destructive',
        title: 'Error al cargar el costo',
        description: 'Ya existe un costo para el rol y la experiencia seleccionada',
      });
      return;
    }

    const res = await fetch(FINANZAS_API + FINANZAS_CARGAR_COSTO, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nombre: newCosto.nombre,
        experiencia: newCosto.experiencia,
        anio: newCosto.anio,
        mes: newCosto.mes,
        costo: newCosto.costo,
      }),
    });

    const jsonRes: costos = await res.json();

    if (res.ok) {
      router.push(
        '/costos-rol?id=' +
          jsonRes.id +
          '&nombre=' +
          data.rol +
          '&experiencia=' +
          data.seniority +
          '&costo=' +
          data.sueldo +
          '&editado=false'
      );
    } else {
      console.error('Error al cargar el costo', res);
    }
  };

  return (
    <div className="mb-8">
      <h1 className="text-center text-4xl font-bold mt-8 mb-16">Cargar costo mensual</h1>

      <Card className="w-[500px] mx-auto">
        <CardHeader>
          <CardTitle>{id ? 'Actualizar un costo mensual' : 'Cargar costo mensual'}</CardTitle>
          <CardDescription>
            Seleccione una fecha y el rol para cargar el costo mensual.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form className="space-y-8" onSubmit={form.handleSubmit(onSubmit)}>
              <FormField
                control={form.control}
                name="rol"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Rol</FormLabel>
                    <Popover>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            disabled={!!rol}
                            className={cn(
                              'w-[100%] justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value || 'Seleccionar un rol'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[200px] p-0">
                        <Command>
                          <CommandInput placeholder="Buscar un rol..." />
                          <CommandList>
                            <CommandEmpty>No se encontró ningun rol</CommandEmpty>
                            <CommandGroup>
                              {rolesPosibles.map((rol) => (
                                <CommandItem
                                  value={rol.nombre}
                                  key={rol.nombre}
                                  onSelect={() => {
                                    form.setValue('rol', rol.nombre);
                                    setSelectedRole(rol.nombre);
                                  }}
                                >
                                  {rol.nombre}
                                  <Check
                                    className={cn(
                                      'ml-auto',
                                      rol.nombre === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormDescription>Seleccione el rol que desea cargar.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="seniority"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Experiencia</FormLabel>
                    <Popover>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            disabled={!!experiencia}
                            className={cn(
                              'w-[100%] justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value || 'Seleccionar una experiencia'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[200px] p-0">
                        <Command>
                          <CommandInput placeholder="Buscar un rol..." />
                          <CommandList>
                            <CommandEmpty>No se encontró ninguna experiencia</CommandEmpty>
                            <CommandGroup>
                              {rolesPosibles
                                .find((rol) => rol.nombre === selectedRole)
                                ?.experiencia.map((experiencia) => (
                                  <CommandItem
                                    value={experiencia}
                                    key={experiencia}
                                    onSelect={() => {
                                      form.setValue('seniority', experiencia);
                                    }}
                                  >
                                    {experiencia}
                                    <Check
                                      className={cn(
                                        'ml-auto',
                                        experiencia === field.value ? 'opacity-100' : 'opacity-0'
                                      )}
                                    />
                                  </CommandItem>
                                )) || []}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormDescription>Seleccione la experiencia que desea cargar.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
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
                        className="w-full"
                        customInput={<DateInput disabled={!!mes && !!anio} />}
                      />
                    </FormControl>
                    <FormDescription>Seleccione el mes y el año que desea cargar.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="sueldo"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Sueldo</FormLabel>
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
                    <FormDescription>Ingrese el sueldo que desea cargar.</FormDescription>
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
