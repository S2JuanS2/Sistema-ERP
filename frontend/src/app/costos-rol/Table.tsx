'use client';

import { DataTable } from '@/components/ui/data-table';
import { Mes } from '@/types/enums';
import { ColumnDef } from '@tanstack/react-table';
import { Pencil } from 'lucide-react';
import { useRouter } from 'next/navigation';

export type costosTableData = {
  id: string;
  rol: string;
  seniority: string;
  costo: number;
};

type TableProps = {
  data: costosTableData[];
  mes: string;
  anio: string;
};

export default function Table({ data, mes, anio }: TableProps) {
  const router = useRouter();

  function handleEditCosto(actual: costosTableData) {
    router.push(
      `/cargar-costo-rol?id=${actual.id}&rol=${actual.rol}&experiencia=${actual.seniority}&costo=${
        actual.costo
      }&mes=${Mes[mes as keyof typeof Mes]}&anio=${anio}`
    );
  }

  const columns: ColumnDef<costosTableData>[] = [
    {
      accessorKey: 'rol',
      header: 'Rol',
    },
    {
      accessorKey: 'seniority',
      header: 'Seniority',
    },
    {
      accessorKey: 'costo',
      header: 'Costo por mes',
    },
    {
      id: 'actions',
      header: 'Acciones',
      cell: ({ row }) => (
        <button onClick={() => handleEditCosto(row.original)}>
          <div className="bg-slate-200 p-2 rounded flex items-center text-gray-600 hover:text-black transition-colors">
            <Pencil className="mr-2" size={20} />
            <p className="font-semibold ">Editar</p>
          </div>
        </button>
      ),
    },
  ];

  return <DataTable columns={columns} data={data} />;
}
