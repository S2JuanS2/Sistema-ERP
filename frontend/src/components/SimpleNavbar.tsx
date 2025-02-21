'use client';

import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from '@/components/ui/navigation-menu';
import { PROYECTOS_COSTOS_POR_RECURSO, PROYECTOS_RESUMEN_SEMANAL } from '@/constants';
import Link from 'next/link';

export default function SimpleNavbar() {
  return (
    <nav className="py-2 px-6 flex items-center gap-10 border-b text-white border-b-gray-300 bg-primary">
      <h1 className="text-2xl font-bold mr-6 ">
        <Link
          href="/"
          className="focus-visible:border-2 focus-visible:border-primary focus-visible:outline-none  focus-visible:rounded"
        >
          Praxis Systems Argentina
        </Link>
      </h1>
      <NavigationMenu>
        <NavigationMenuList>
          <NavigationMenuItem>
            <NavigationMenuLink
              href={PROYECTOS_RESUMEN_SEMANAL}
              className={`${navigationMenuTriggerStyle()} text-[16px] hover:cursor-pointer bg-gray-100 hover:bg-gray-200 focus:border-2 focus:border-primary`}
            >
              Resumen Semanal
            </NavigationMenuLink>
          </NavigationMenuItem>
          <NavigationMenuItem>
            <NavigationMenuLink
              href={PROYECTOS_COSTOS_POR_RECURSO}
              className={`${navigationMenuTriggerStyle()} text-[16px] hover:cursor-pointer bg-gray-100 hover:bg-gray-200 focus:border-2 focus:border-primary`}
            >
              Costos por recurso
            </NavigationMenuLink>
          </NavigationMenuItem>

          <NavigationMenuItem>
            <NavigationMenuLink
              href="/costos-rol"
              className={`${navigationMenuTriggerStyle()} text-[16px] hover:cursor-pointer bg-gray-100 hover:bg-gray-200 focus:border-2 focus:border-primary`}
            >
              Costos de los roles
            </NavigationMenuLink>
          </NavigationMenuItem>
          <NavigationMenuItem>
            <NavigationMenuLink
              href="/costos"
              className={`${navigationMenuTriggerStyle()} text-[16px] hover:cursor-pointer bg-gray-100 hover:bg-gray-200 focus:border-2 focus:border-primary`}
            >
              Costos de los proyectos
            </NavigationMenuLink>
          </NavigationMenuItem>
        </NavigationMenuList>
      </NavigationMenu>
    </nav>
  );
}
