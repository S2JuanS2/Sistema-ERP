import Navbar from '@/components/Navbar';
import ClientComponent from './ClientComponent';
import { FINANZAS_API, FINANZAS_COSTOS, MESES } from '@/constants';
import { costos } from '@/types/costos';
import { Suspense } from 'react';

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

async function fetchData() {
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

  return data;
}

export default async function Page() {
  const data = await fetchData();
  return (
    <div>
      <Navbar />
      <Suspense>
        <ClientComponent data={data} fechasPosibles={obtenerFechasPosibles(data)} />
      </Suspense>
    </div>
  );
}
