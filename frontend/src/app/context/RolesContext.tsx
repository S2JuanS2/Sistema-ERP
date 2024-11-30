'use client';

import { FINANZAS_API, FINANZAS_COSTOS } from '@/constants';
import { costos } from '@/types/costos';
import { createContext, useContext, useEffect, useState } from 'react';

const RolesContext = createContext<{
  data: costos[];
  addCostos: (costos: costos[]) => void;
  editCostos: (costos: costos[]) => void;
  editSingleCosto: (costo: costos) => void;
} | null>(null);

export const RolesProvider = ({ children }: { children: React.ReactNode }) => {
  const [data, setData] = useState<costos[]>([]);

  const addCostos = (costos: costos[]) => {
    console.log('AGREGANDO COSTO!');
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
        const res = await fetch(FINANZAS_API + FINANZAS_COSTOS);

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
      } catch (error) {
        console.error(error);
      }
    }

    fetchData();
  }, []);

  return (
    <RolesContext.Provider value={{ data, addCostos, editCostos, editSingleCosto }}>
      {children}
    </RolesContext.Provider>
  );
};

export const useRoles = (): {
  data: costos[];
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
