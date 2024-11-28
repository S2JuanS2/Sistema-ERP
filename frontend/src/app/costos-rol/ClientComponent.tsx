'use client';

import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { MESES } from '@/constants';
import { costos } from '@/types/costos';
import { ChevronDown } from 'lucide-react';
import Link from 'next/link';
import { useEffect, useState } from 'react';
import { fechasPosibles } from './page';
import { useSearchParams } from 'next/navigation';
import { useToast } from '@/hooks/use-toast';
import Table, { costosTableData } from './Table';

export default function ClientComponent({
  data,
  fechasPosibles,
}: {
  data: costos[];
  fechasPosibles: fechasPosibles[];
}) {
  const [period, setPeriod] = useState({
    year: fechasPosibles[0] ? fechasPosibles[0].anio : new Date().getFullYear().toString(),
    month: fechasPosibles[0] ? fechasPosibles[0].meses[0] : MESES[new Date().getMonth()],
  });

  const { toast } = useToast();
  const searchParams = useSearchParams();

  useEffect(() => {
    const id = searchParams.get('id');
    const nombre = searchParams.get('nombre');
    const experiencia = searchParams.get('experiencia');
    const costo = searchParams.get('costo');
    const editado = searchParams.get('editado');

    // * NOTA: Esto es solo para que se actualice en tiempo real, cuando se haga refresh solo se usa la obtenida de la API
    if (nombre && experiencia && costo) {
      if (editado) {
        const index = data.findIndex((costo) => costo.id.toString() === id);
        data[index].costo = parseInt(costo);
      } else {
        data.push({
          id: id?.toString() || '',
          anio: period.year,
          mes: period.month,
          costo: parseInt(costo),
          rol: {
            id: data.length + 1,
            nombre: nombre,
            experiencia: experiencia,
          },
        });
      }

      setTimeout(() => {
        toast({
          title: `Se ${editado ? 'actualizó' : 'agregó'} el costo correctamente`,
          description: `Se ${
            editado ? 'actualizó' : 'agregó'
          } el costo ${costo} al rol ${nombre.toLowerCase()} con experiencia ${experiencia.toLowerCase()}`,
        });
      }, 100);
    }
  }, [searchParams, toast, period.year, period.month, data]);

  function obtenerCostos(): costosTableData[] {
    const costos: costosTableData[] = [];
    data.forEach((costo) => {
      if (
        costo.anio === period.year.toString() &&
        MESES[parseInt(costo.mes) - 1] === period.month
      ) {
        costos.push({
          id: costo.id,
          rol: costo.rol.nombre,
          seniority: costo.rol.experiencia,
          costo: costo.costo,
        });
      }
    });
    return costos;
  }

  function handleChangeYear(year: string) {
    const newMonth = fechasPosibles.find((fecha) => fecha.anio === year)?.meses[0];
    setPeriod({ year, month: newMonth || period.month });
  }

  return (
    <div className="container mx-auto my-6">
      <h1 className="text-center text-4xl font-bold mt-8 mb-16">Costos de los roles</h1>

      <main className="flex flex-col gap-8 justify-center align-center">
        <div className="flex w-full justify-between">
          <div className="flex gap-4 items-center ">
            <div className="flex items-center gap-2 mr-6">
              <p className="font-semibold text-lg">Año:</p>
              <DropdownMenu>
                <DropdownMenuTrigger className="p-2 rounded-md flex items-center border focus:outline-primary">
                  {period.year}
                  <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  {fechasPosibles.map((fecha) => (
                    <DropdownMenuItem key={fecha.anio} onClick={() => handleChangeYear(fecha.anio)}>
                      {fecha.anio}
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
            <div className="flex items-center gap-2">
              <p className="font-semibold text-lg">Mes:</p>
              <DropdownMenu>
                <DropdownMenuTrigger className="p-2 rounded-md flex items-center border focus:outline-primary">
                  {period.month}
                  <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  {fechasPosibles
                    .find((fecha) => fecha.anio === period.year)
                    ?.meses.map((mes) => (
                      <DropdownMenuItem
                        key={mes}
                        onClick={() => setPeriod({ ...period, month: mes })}
                      >
                        {mes}
                      </DropdownMenuItem>
                    ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
          </div>
          <Link href={'/cargar-costo-rol'}>
            <Button>Cargar Costo</Button>
          </Link>
        </div>
        <div>
          <Table data={obtenerCostos()} mes={period.month} anio={period.year} />
        </div>
      </main>
    </div>
  );
}
