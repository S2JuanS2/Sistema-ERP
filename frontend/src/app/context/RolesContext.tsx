'use client';

import { FINANZAS_API, FINANZAS_COSTOS } from '@/constants';
import { costos } from '@/types/costos';
import { createContext, useContext, useEffect, useState } from 'react';

const RolesContext = createContext<{
  data: costos[];
  setData: React.Dispatch<React.SetStateAction<costos[]>>;
}>({
  data: [],
  setData: () => {},
});

export const RolesProvider = ({ children }: { children: React.ReactNode }) => {
  const [data, setData] = useState<costos[]>([]);

  //! NOTA: Este fetch se podria hacer usando Sever Side Rendering pero no encontré la forma de hacerlo funcionar
  useEffect(() => {
    async function fetchData() {
      try {
        const res = await fetch(FINANZAS_API + FINANZAS_COSTOS, {
          cache: 'no-store',
        });

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

  return <RolesContext.Provider value={{ data, setData }}>{children}</RolesContext.Provider>;
};

export const useRoles = (): {
  data: costos[];
  setData: React.Dispatch<React.SetStateAction<costos[]>>;
} => {
  const context = useContext(RolesContext);

  if (!context) {
    throw new Error('useRoles must be used within a RolesProvider');
  }
  return context;
};
