import type { Metadata } from 'next';
import { Toaster } from '@/components/ui/toaster';
import localFont from 'next/font/local';
import './globals.css';

const geistSans = localFont({
  src: './fonts/GeistVF.woff',
  variable: '--font-geist-sans',
  weight: '100 900',
});
const geistMono = localFont({
  src: './fonts/GeistMonoVF.woff',
  variable: '--font-geist-mono',
  weight: '100 900',
});

export const metadata: Metadata = {
  title: 'PSA ERP',
  description: 'PSA Cloud Spring ERP',
};

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
      <body
        className={`${geistSans.variable} ${geistMono.variable} flex flex-col antialiased h-100  min-h-screen`}
      >
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
