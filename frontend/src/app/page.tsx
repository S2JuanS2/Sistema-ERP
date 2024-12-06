'use client';

import Footer from '@/components/Footer';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';

export default function Home() {
  return (
    <div className="min-h-screen flex flex-col">
      {/* Header */}
      <header className="py-8 text-center">
        <div className="container mx-auto px-4">
          <h1 className="text-4xl font-bold">Praxis Systems Argentina</h1>
          <p className="mt-2 text-2xl">
            Innovación en software empresarial para Sudamérica de habla hispana.
          </p>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 container mx-auto px-4 py-8 space-y-8">
        {/* Overview Section */}
        <section>
          <h2 className="text-2xl font-semibold mb-4">Sobre Nosotros</h2>
          <p className="text-lg leading-relaxed text-balance">
            Con más de 15 años en el mercado, PSA se especializa en el desarrollo de software ERP,
            CRM y BI, brindando soluciones a empresas medianas y grandes en Sudamérica. Nuestro
            equipo de 450 empleados opera desde oficinas en Buenos Aires, Santiago de Chile y Lima,
            trabajando para garantizar la excelencia en cada proyecto.
          </p>
        </section>

        {/* Services Section */}
        <section className="py-8">
          <h2 className="text-2xl font-semibold mb-6 text-center">Nuestros Productos</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {/* PSA Cloud Spring ERP */}
            <Card>
              <CardHeader>
                <CardTitle>PSA Cloud Spring ERP</CardTitle>
                <CardDescription>Gestión empresarial avanzada en la nube.</CardDescription>
              </CardHeader>
              <CardContent>
                <p>
                  Diseñado para empresas que buscan optimizar sus operaciones con una solución
                  segura y escalable.
                </p>
              </CardContent>
            </Card>

            {/* PSA Spring CRM */}
            <Card>
              <CardHeader>
                <CardTitle>PSA Spring CRM</CardTitle>
                <CardDescription>
                  Solución para la gestión de relaciones con clientes.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <p>
                  Potencia la interacción con tus clientes y mejora la eficiencia de tu equipo
                  comercial.
                </p>
              </CardContent>
            </Card>

            {/* PSA Business Analytics */}
            <Card>
              <CardHeader>
                <CardTitle>PSA Business Analytics</CardTitle>
                <CardDescription>
                  Inteligencia de negocio para decisiones estratégicas.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <p>
                  Analiza tus datos y obtén insights clave para llevar tu empresa al siguiente
                  nivel.
                </p>
              </CardContent>
            </Card>
          </div>
        </section>

        <section>
          <h2 className="text-2xl font-semibold mb-4 text-center">Nuestro Equipo</h2>

          <Carousel className=" p-4 w-full">
            <CarouselContent className="-ml-8">
              <CarouselItem className=" basis-1/3 pl-8">
                <div className="border-2 border-gray-200 rounded-lg px-4 py-8">
                  <strong className="text-2xl">Juan Zeo</strong>
                  <p className="text-lg">
                    Socio fundador y CEO. Visionario detrás de nuestra estrategia.
                  </p>
                </div>
              </CarouselItem>
              <CarouselItem className="rounded-lg basis-1/3 pl-8">
                <div className="border-2 border-gray-200 rounded-lg px-4 py-8">
                  <strong className="text-2xl">Fernando Soluzzia</strong>
                  <p className="text-lg">
                    Gerente de Operaciones. Líder del desarrollo de PSA Cloud Spring ERP.
                  </p>
                </div>
              </CarouselItem>
              <CarouselItem className="rounded-lg basis-1/3 pl-8">
                <div className="border-2 border-gray-200 rounded-lg px-4 py-8">
                  <strong className="text-2xl">José Mercado</strong>
                  <p className="text-lg">
                    Gerente de Marketing. Experto en tendencias y estrategias de mercado.
                  </p>
                </div>
              </CarouselItem>
              <CarouselItem className=" rounded-lg basis-1/3 pl-8">
                <div className="border-2 border-gray-200 rounded-lg px-4 py-8">
                  <strong className="text-2xl">Juan Anvizzio</strong>
                  <p className="text-lg">
                    Gerente de Ventas. Responsable del crecimiento comercial.
                  </p>
                </div>
              </CarouselItem>
              <CarouselItem className=" rounded-lg basis-1/3 pl-8">
                <div className="border-2 border-gray-200 rounded-lg px-4 py-8">
                  <strong className="text-2xl">Roberto Ratio</strong>
                  <p className="text-lg">
                    Gerente de Administración y Finanzas. Experto en planeamiento financiero.
                  </p>
                </div>
              </CarouselItem>
            </CarouselContent>
            <CarouselPrevious />
            <CarouselNext />
          </Carousel>
        </section>
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
}
