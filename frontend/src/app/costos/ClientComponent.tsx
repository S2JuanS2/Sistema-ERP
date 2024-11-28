'use client';

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { FINANZAS_API, FINANZAS_PROYECTOS, MESES } from '@/constants';
import { ChevronDown } from 'lucide-react';
import { useEffect, useState } from 'react';
import Table from './Table';
import { Proyectos } from '@/types/proyectosAPI';
import { fetchWithTimeout } from '@/lib/fetchWithTimeout';

const mockData: Proyectos = {
  proyectos: [
    {
      nombreProyecto: 'Desarrollo Plataforma e-Commerce',
      costoPorMes: {
        '1': 3000,
        '2': 3200,
        '3': 3100,
        '4': 3050,
        '5': 3150,
        '6': 3250,
        '7': 3300,
        '8': 3200,
        '9': 3350,
        '10': 3400,
        '11': 3500,
        '12': 3450,
      },
      costoTotal: 38950,
    },
    {
      nombreProyecto: 'Sistema de Gestión de Inventarios',
      costoPorMes: {
        '1': 3000,
        '2': 3050,
        '3': 3000,
        '4': 2970,
        '5': 3010,
        '6': 3100,
        '7': 3050,
        '8': 3120,
        '9': 3200,
        '10': 3300,
        '11': 3350,
        '12': 3400,
      },

      costoTotal: 37550,
    },
    {
      nombreProyecto: 'Implementación Red de Ciberseguridad',
      costoPorMes: {
        '1': 3500,
        '2': 3600,
        '3': 3550,
        '4': 3520,
        '5': 3450,
        '6': 3400,
        '7': 3480,
        '8': 3550,
        '9': 3600,
        '10': 3700,
        '11': 3750,
        '12': 3800,
      },
      costoTotal: 42900,
    },
  ],
  costoTotalGlobal: 119400,
};

// Años posibles: desde el 2000 hasta la actualidad ordenados de forma descendente
const years = Array.from(
  { length: new Date().getFullYear() - 1999 },
  (_, i) => new Date().getFullYear() - i
);

export type Period = {
  year: number;
  from: string;
  to: string;
};

export default function ClientComponent() {
  const [period, setPeriod] = useState({
    year: new Date().getFullYear(),
    from: 'Enero',
    to: 'Diciembre',
  });
  const [projectsData, setProjectsData] = useState<Proyectos>();

  const fetchData = async (year: string | number) => {
    try {
      const response = await fetchWithTimeout(FINANZAS_API + FINANZAS_PROYECTOS + year);
      const data = await response.json();
      setProjectsData(data);
    } catch (error) {
      console.error(error);
      setProjectsData(mockData);
    }
  };

  useEffect(() => {
    fetchData(period.year);
  }, []);

  const handleChangeYear = (year: number) => {
    setPeriod({ ...period, year });
    fetchData(year);
  };

  return (
    <div className="container mx-auto my-6">
      <h1 className="text-center text-4xl font-bold mt-8 mb-16">Costos de los proyectos</h1>

      <main className="flex flex-col gap-8 row-start-2 items-center sm:items-start">
        <div className="flex justify-between w-full">
          <div className="flex gap-4 items-center">
            <div className="flex items-center gap-2 mr-6">
              <p className="font-semibold text-lg">Año:</p>
              <DropdownMenu>
                <DropdownMenuTrigger className="p-2 rounded-md flex items-center border focus:outline-primary">
                  {period.year}
                  <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </DropdownMenuTrigger>
                <DropdownMenuContent className="max-h-40 overflow-y-auto">
                  {years.map((year) => (
                    <DropdownMenuItem key={year} onClick={() => handleChangeYear(year)}>
                      {year}
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
            <div className="flex items-center gap-2">
              <p className="font-semibold text-lg">Desde:</p>
              <DropdownMenu>
                <DropdownMenuTrigger className="p-2 rounded-md flex items-center border focus:outline-primary">
                  {period.from}
                  <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  {MESES.map((mes) => (
                    <DropdownMenuItem key={mes} onClick={() => setPeriod({ ...period, from: mes })}>
                      {mes}
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
            <div className="flex items-center gap-2">
              <p className="font-semibold text-lg">Hasta:</p>

              <DropdownMenu>
                <DropdownMenuTrigger className="p-2 rounded-md flex items-center border focus:outline-primary">
                  {period.to}
                  <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  {MESES.map((mes) => (
                    <DropdownMenuItem key={mes} onClick={() => setPeriod({ ...period, to: mes })}>
                      {mes}
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
          </div>
        </div>
        <Table data={projectsData?.proyectos || []} period={period} />
        <p className="text-lg font-bold">
          Costo total:
          <span className="ms-2 font-normal">${projectsData?.costoTotalGlobal || 0}</span>
        </p>
      </main>
    </div>
  );
}
