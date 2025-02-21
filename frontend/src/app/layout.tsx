import type { Metadata } from 'next';
import { Toaster } from '@/components/ui/toaster';
import { Montserrat } from 'next/font/google';
import './globals.css';

export const metadata: Metadata = {
  title: 'PSA ERP',
  description: 'PSA Cloud Spring ERP',
};

const montserrat = Montserrat({ subsets: ['latin'], variable: '--font-montserrat' });

import 'react-datepicker/dist/react-datepicker.css';
import SimpleFooter from '@/components/SimpleFooter';
import SimpleNavbar from '@/components/SimpleNavbar';
import { RolesProvider } from './context/RolesContext';

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${montserrat.variable} flex flex-col antialiased h-100  min-h-screen`}>
        <SimpleNavbar />
        <RolesProvider>
          <div className="flex-1">{children}</div>
        </RolesProvider>
        <SimpleFooter />
        <Toaster />
      </body>
    </html>
  );
}
