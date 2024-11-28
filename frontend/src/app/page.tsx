'use client';

import Footer from '@/components/Footer';
import Navbar from '@/components/Navbar';

export default function Home() {
  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />
      <div className="flex-1 ">
        <h1>Praxis Systems Argentina</h1>
      </div>
      <Footer />
    </div>
  );
}
