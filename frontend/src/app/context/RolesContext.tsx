'use client';

import { FINANZAS_API, FINANZAS_COSTOS } from '@/constants';
import { useToast } from '@/hooks/use-toast';
import { fetchWithTimeout } from '@/lib/fetchWithTimeout';
import { costos } from '@/types/costos';
import { createContext, useContext, useEffect, useState } from 'react';

const RolesContext = createContext<{
  data: costos[];
  loading: boolean;
  addCostos: (costos: costos[]) => void;
  editCostos: (costos: costos[]) => void;
  editSingleCosto: (costo: costos) => void;
} | null>(null);

export const RolesProvider = ({ children }: { children: React.ReactNode }) => {
  const [data, setData] = useState<costos[]>([]);
  const [loading, setLoading] = useState(true);
  const toast = useToast();

  const addCostos = (costos: costos[]) => {
    setData((prev) => [...prev, ...costos]);
  };

  const editCostos = (costos: costos[]) => {
    setData((prev) => {
      const newData = [...prev];
      costos.forEach((costo) => {
        const index = newData.findIndex(
          (c) =>
            c.rol.nombre === costo.rol.nombre &&
            c.rol.experiencia === costo.rol.experiencia &&
            c.mes === costo.mes &&
            c.anio === costo.anio
        );
        if (index !== -1) {
          newData[index] = costo;
        }
      });
      return newData;
    });
  };

  const editSingleCosto = (costo: costos) => {
    setData((prev) => {
      const newData = [...prev];
      const index = newData.findIndex(
        (c) =>
          c.rol.nombre === costo.rol.nombre &&
          c.rol.experiencia === costo.rol.experiencia &&
          c.mes === costo.mes &&
          c.anio === costo.anio
      );
      if (index !== -1) {
        newData[index] = costo;
      }
      return newData;
    });
  };

  //! NOTA: Este fetch se podria hacer usando Sever Side Rendering pero no encontré la forma de hacerlo funcionar
  useEffect(() => {
    async function fetchData() {
      try {
        const res = await fetchWithTimeout(FINANZAS_API + FINANZAS_COSTOS);

        if (!res.ok) {
          throw new Error('Failed to fetch data');
        }

        const data: costos[] = await res.json();
        data.sort((a, b) => {
          if (a.rol.nombre < b.rol.nombre) {
            return -1;
          }
          if (a.rol.nombre > b.rol.nombre) {
            return 1;
          }
          return 0;
        });

        setData(data);
        setLoading(false);
      } catch (error) {
        console.error(error);
        toast.toast({
          title: 'Error',
          description: 'No se pudo cargar los costos',
          variant: 'destructive',
        });
      }
    }

    fetchData();
  }, []);

  return (
    <RolesContext.Provider value={{ data, loading, addCostos, editCostos, editSingleCosto }}>
      {children}
    </RolesContext.Provider>
  );
};

export const useRoles = (): {
  data: costos[];
  loading: boolean;
  addCostos: (costos: costos[]) => void;
  editCostos: (costos: costos[]) => void;
  editSingleCosto: (costo: costos) => void;
} => {
  const context = useContext(RolesContext);

  if (!context) {
    throw new Error('useRoles must be used within a RolesProvider');
  }
  return context;
};
