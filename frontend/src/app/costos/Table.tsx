'use client';

import { DataTable } from '@/components/ui/data-table';
import { Proyecto } from '@/types/proyectosAPI';
import { ColumnDef } from '@tanstack/react-table';
import { Period } from './ClientComponent';
import { Mes } from '@/types/enums';

type TableProps = {
  data: Proyecto[];
  period: Period;
};

export default function Table({ data, period }: TableProps) {
  const columns: ColumnDef<Proyecto>[] = [
    {
      id: 'nombreProyecto',
      accessorKey: 'nombreProyecto',
      header: 'Nombre',
    },
    {
      accessorFn: (row) => row.costoPorMes['1'],
      id: 'enero',
      header: 'Enero',
    },
    {
      accessorFn: (row) => row.costoPorMes['2'],
      id: 'febrero',
      header: 'Febrero',
    },
    {
      accessorFn: (row) => row.costoPorMes['3'],
      id: 'marzo',
      header: 'Marzo',
    },
    {
      accessorFn: (row) => row.costoPorMes['4'],
      id: 'abril',
      header: 'Abril',
    },
    {
      accessorFn: (row) => row.costoPorMes['5'],
      id: 'mayo',
      header: 'Mayo',
    },
    {
      accessorFn: (row) => row.costoPorMes['6'],
      id: 'junio',
      header: 'Junio',
    },
    {
      accessorFn: (row) => row.costoPorMes['7'],
      id: 'julio',
      header: 'Julio',
    },
    {
      accessorFn: (row) => row.costoPorMes['8'],
      id: 'agosto',
      header: 'Agosto',
    },
    {
      accessorFn: (row) => row.costoPorMes['9'],
      id: 'septiembre',
      header: 'Septiembre',
    },
    {
      accessorFn: (row) => row.costoPorMes['10'],
      id: 'octubre',
      header: 'Octubre',
    },
    {
      accessorFn: (row) => row.costoPorMes['11'],
      id: 'noviembre',
      header: 'Noviembre',
    },
    {
      accessorFn: (row) => row.costoPorMes['12'],
      id: 'diciembre',
      header: 'Diciembre',
    },
    {
      accessorFn: (row) => row.costoTotal,
      id: 'total',
      header: 'Total',
    },
  ];

  const fromMonth = Mes[period.from as keyof typeof Mes];
  const toMonth = Mes[period.to as keyof typeof Mes];

  const filteredColumns = columns.filter((column) => {
    if (column.id === 'total' || column.id === 'nombreProyecto') {
      return true;
    }

    const monthNumber = Mes[column.header as keyof typeof Mes];
    return monthNumber >= fromMonth && monthNumber <= toMonth;
  });

  // Update data total to match selected period
  data = data.map((project) => {
    const costoPorMes = Object.entries(project.costoPorMes)
      .filter(([month]) => {
        const monthNumber = parseInt(month);
        return monthNumber >= fromMonth && monthNumber <= toMonth;
      })
      .reduce((acc: Proyecto['costoPorMes'], [month, value]) => {
        acc[month] = value;
        return acc;
      }, {});

    const costoTotal = Object.values(costoPorMes).reduce((acc, value) => acc + value, 0);

    return {
      ...project,
      costoPorMes,
      costoTotal,
    };
  });

  return <DataTable columns={filteredColumns} data={data} />;
}
