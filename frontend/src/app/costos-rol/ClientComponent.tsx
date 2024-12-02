'use client';

import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { MESES } from '@/constants';
import { ChevronDown, UserRoundPen, UserRoundPlusIcon } from 'lucide-react';
import Link from 'next/link';
import { useEffect, useMemo, useState } from 'react';
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
  const { data, loading } = useRoles();
  const fechasPosibles = useMemo(() => {
    return obtenerFechasPosibles(data);
  }, [data]);

  const [period, setPeriod] = useState({
    year: new Date().getFullYear().toString(),
    month: MESES[new Date().getMonth()],
  });

  useEffect(() => {
    setPeriod({
      year: fechasPosibles[0] ? fechasPosibles[0].anio : new Date().getFullYear().toString(),
      month: fechasPosibles[0] ? fechasPosibles[0].meses[0] : MESES[new Date().getMonth()],
    });
  }, [data, fechasPosibles]);

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
          <div className="flex  gap-4">
            <Button className="font-semibold" asChild>
              <Link
                href={{
                  pathname: '/cargar-costo-rol',
                  query: {
                    mes: period.month,
                    anio: period.year,
                  },
                }}
              >
                <UserRoundPlusIcon size={20} />
                Cargar Costos
              </Link>
            </Button>
            <Button className="font-semibold" asChild>
              <Link
                href={{
                  pathname: '/cargar-costo-rol',
                  query: {
                    mes: period.month,
                    anio: period.year,
                    editar: true,
                  },
                }}
              >
                <UserRoundPen size={20} />
                Editar costos
              </Link>
            </Button>
          </div>
        </div>
        <div>
          <Table data={obtenerCostos()} loading={loading} />
        </div>
      </main>
    </div>
  );
}
