import { Suspense } from 'react';
import ClientComponent from './ClientComponent';

export default function Page() {
  return (
    <Suspense>
      <ClientComponent />
    </Suspense>
  );
}
