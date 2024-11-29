import ClientComponent from './ClientComponent';
import { Suspense } from 'react';

export default async function Page() {
  return (
    <Suspense>
      <ClientComponent />
    </Suspense>
  );
}
