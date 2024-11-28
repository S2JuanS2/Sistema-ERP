'use client';

import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from '@/components/ui/navigation-menu';
import Link from 'next/link';

export default function SimpleNavbar() {
  return (
    <nav className="mt-2 py-2 px-4 flex items-center gap-8 border-b border-b-gray-300 bg-gray-100">
      <h1 className="text-2xl font-bold mr-6">
        <Link href="/">Praxis Systems Argentina</Link>
      </h1>
      <NavigationMenu>
        <NavigationMenuList>
          <NavigationMenuItem>
            <NavigationMenuLink
              className={`${navigationMenuTriggerStyle()} text-[16px] font-semibold hover:cursor-pointer bg-gray-100 hover:bg-gray-200`}
            >
              Resumen Semanal
            </NavigationMenuLink>
          </NavigationMenuItem>
          <NavigationMenuItem>
            <NavigationMenuLink
              className={`${navigationMenuTriggerStyle()} text-[16px] font-semibold hover:cursor-pointer bg-gray-100 hover:bg-gray-200`}
            >
              Costos por recurso
            </NavigationMenuLink>
          </NavigationMenuItem>

          <NavigationMenuItem>
            <NavigationMenuLink
              href="/costos-rol"
              className={`${navigationMenuTriggerStyle()} text-[16px] font-semibold hover:cursor-pointer bg-gray-100 hover:bg-gray-200`}
            >
              Costos de los roles
            </NavigationMenuLink>
          </NavigationMenuItem>
          <NavigationMenuItem>
            <NavigationMenuLink
              href="/costos"
              className={`${navigationMenuTriggerStyle()} text-[16px] font-semibold hover:cursor-pointer bg-gray-100 hover:bg-gray-200`}
            >
              Costos de los proyectos
            </NavigationMenuLink>
          </NavigationMenuItem>
        </NavigationMenuList>
      </NavigationMenu>
    </nav>
  );
}
