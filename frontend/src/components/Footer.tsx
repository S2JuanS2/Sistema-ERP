import { Mail, Phone, Pin } from 'lucide-react';

export default function Footer() {
  return (
    <div className="bg-gray-100 mt-6">
      <footer className="border-t border-t-gray-300 text-black pt-2">
        <div className="container mx-16 flex flex-row justify-evenly py-2">
          <div className="flex flex-col gap-1">
            <p className="font-bold ">Oficinas</p>
            <p className="flex items-center">
              <Pin size={16} className="mr-2" /> Buenos Aires, Santiago de Chile, Lima{' '}
            </p>
          </div>
          <div className="flex flex-col gap-1">
            <p className="font-bold">Contacto</p>
            <p className="flex items-center">
              <Mail size={16} className="mr-2" />
              <a href="mailto:soporte@praxis.com.ar">soporte@praxis.com.ar</a>
            </p>
            <p className="flex items-center">
              <Phone size={16} className="mr-2" />
              +54 11 5555-1234
            </p>
          </div>
          <div className="flex flex-col gap-1">
            <p className="font-bold ">Acerca de nosotros</p>
            <p>Nuestra historia</p>
            <p>Políticas de privacidad</p>
            <p>Terminos y condiciones </p>
          </div>
        </div>
      </footer>
    </div>
  );
}
