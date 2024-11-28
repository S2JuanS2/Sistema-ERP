import Navbar from '@/components/Navbar';
import ClientComponent from './ClientComponent';
import { FINANZAS_API, FINANZAS_COSTOS, ROLES_API } from '@/constants';
import { roles } from '@/types/rolesAPI';
import { Suspense } from 'react';

export type rolesPosibles = {
  nombre: string;
  experiencia: string[];
};

async function obtenerRolesPosibles() {
  const response = await fetch(ROLES_API, {
    cache: 'no-store',
  });
  const data: roles[] = await response.json();

  const rolesPosibles: rolesPosibles[] = [];

  data.forEach((rol) => {
    const rolExistente = rolesPosibles.find((r) => r.nombre === rol.nombre);
    if (rolExistente) {
      rolExistente.experiencia.push(rol.experiencia);
    } else {
      rolesPosibles.push({
        nombre: rol.nombre,
        experiencia: [rol.experiencia],
      });
    }
  });

  return rolesPosibles;
}

async function fetchData() {
  const res = await fetch(FINANZAS_API + FINANZAS_COSTOS, {
    cache: 'no-store',
  });

  if (!res.ok) {
    throw new Error('Failed to fetch data');
  }

  return res.json();
}

export default async function page() {
  const roles = await obtenerRolesPosibles();
  const data = await fetchData();
  return (
    <div>
      <Navbar />
      <Suspense>
        <ClientComponent rolesPosibles={roles} registeredData={data} />
      </Suspense>
    </div>
  );
}
