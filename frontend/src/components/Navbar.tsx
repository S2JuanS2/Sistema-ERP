'use client';

import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
} from '@/components/ui/navigation-menu';
import { cn } from '@/lib/utils';
import Link from 'next/link';
import { forwardRef } from 'react';

const finanzas: { title: string; href: string; description: string }[] = [
  {
    title: 'Costos de Proyectos',
    href: '/costos',
    description: 'Visualizar los costos de todos los proyectos, filtrado por fecha',
  },
  {
    title: 'Costos de Roles',
    href: '/costos-rol',
    description: 'Visualizar, crear y modificar los costos mensuales de cada rol',
  },
];

export default function Navbar() {
  return (
    <nav className="mt-2 py-2 px-4 flex items-center gap-8 border-b border-b-gray-300">
      <h1 className="text-2xl font-bold mr-6">
        <Link href="/">Praxis Systems Argentina</Link>
      </h1>
      <NavigationMenu>
        <NavigationMenuList>
          <NavigationMenuItem>
            <NavigationMenuTrigger className="text-md font-semibold">
              Finanzas
            </NavigationMenuTrigger>
            <NavigationMenuContent>
              <ul className="grid w-[400px] gap-3 p-4 md:grid-rows-2 ">
                {finanzas.map((item) => (
                  <ListItem key={item.title} title={item.title} href={item.href}>
                    {item.description}
                  </ListItem>
                ))}
              </ul>
            </NavigationMenuContent>
          </NavigationMenuItem>
        </NavigationMenuList>
      </NavigationMenu>
    </nav>
  );
}

const ListItem = forwardRef<React.ElementRef<'a'>, React.ComponentPropsWithoutRef<'a'>>(
  ({ className, title, children, ...props }, ref) => {
    return (
      <li>
        <NavigationMenuLink asChild>
          <a
            ref={ref}
            className={cn(
              'block select-none space-y-1 rounded-md p-3 leading-none no-underline outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground',
              className
            )}
            {...props}
          >
            <div className="text-sm font-medium leading-none">{title}</div>
            <p className="line-clamp-2 text-sm leading-snug text-muted-foreground">{children}</p>
          </a>
        </NavigationMenuLink>
      </li>
    );
  }
);
ListItem.displayName = 'ListItem';
