'use client';

import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { MESES } from '@/constants';
import { ChevronDown, UserPlus } from 'lucide-react';
import Link from 'next/link';
import { useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'next/navigation';
import { useToast } from '@/hooks/use-toast';
import Table, { costosTableData } from './Table';
import { useRoles } from '../context/RolesContext';
import { costos } from '@/types/costos';

export type fechasPosibles = {
  anio: string;
  meses: string[];
};

function obtenerFechasPosibles(data: costos[]): fechasPosibles[] {
  // Meses posibles de cada año
  const mesesPosibles: fechasPosibles[] = [];
  data.forEach((costo: costos) => {
    const anioYaRegistrado = mesesPosibles.find((element) => element.anio === costo.anio);
    const mesYaRegistrado = anioYaRegistrado?.meses.find(
      (element) => element === MESES[parseInt(costo.mes) - 1]
    );

    if (anioYaRegistrado) {
      if (!mesYaRegistrado) {
        anioYaRegistrado.meses.push(MESES[parseInt(costo.mes) - 1]);
      }
    } else {
      mesesPosibles.push({ anio: costo.anio, meses: [MESES[parseInt(costo.mes) - 1]] });
    }
  });

  // Ordenar los meses
  mesesPosibles.forEach((element) => {
    element.meses.sort((a, b) => MESES.indexOf(a) - MESES.indexOf(b));
  });

  // Ordenar los años
  mesesPosibles.sort((a, b) => parseInt(b.anio) - parseInt(a.anio));

  return mesesPosibles;
}

export default function ClientComponent() {
  const { data } = useRoles();
  const fechasPosibles = useMemo(() => {
    return obtenerFechasPosibles(data);
  }, [data]);

  const [period, setPeriod] = useState({
    year: new Date().getFullYear().toString(),
    month: MESES[new Date().getMonth()],
  });

  const { toast } = useToast();
  const searchParams = useSearchParams();

  useEffect(() => {
    setPeriod({
      year: fechasPosibles[0] ? fechasPosibles[0].anio : new Date().getFullYear().toString(),
      month: fechasPosibles[0] ? fechasPosibles[0].meses[0] : MESES[new Date().getMonth()],
    });
  }, [data, fechasPosibles]);

  useEffect(() => {
    const id = searchParams.get('id');
    const nombre = searchParams.get('nombre');
    const experiencia = searchParams.get('experiencia');
    const costo = searchParams.get('costo');
    const editado = searchParams.get('editado') === 'true';

    // * NOTA: Esto es solo para que se actualice en tiempo real, cuando se haga refresh solo se usa la obtenida de la API
    //! NOTA2: Esto ya no deberia ser util en la neuva implementación ya que usa context.
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
            <Button className="font-semibold">
              <UserPlus size={20} />
              Cargar Costo
            </Button>
          </Link>
        </div>
        <div>
          <Table data={obtenerCostos()} mes={period.month} anio={period.year} />
        </div>
      </main>
    </div>
  );
}
