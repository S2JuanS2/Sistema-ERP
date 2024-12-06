import ClientComponent from './ClientComponent';
import { ROLES_API } from '@/constants';
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

export default async function page() {
  const roles = await obtenerRolesPosibles();
  return (
    <Suspense>
      <ClientComponent rolesPosibles={roles} />
    </Suspense>
  );
}
