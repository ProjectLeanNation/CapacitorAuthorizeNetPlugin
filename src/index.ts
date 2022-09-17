import { registerPlugin } from '@capacitor/core';

import type { AuthorizeNetPlugin } from './definitions';

const AuthorizeNet = registerPlugin<AuthorizeNetPlugin>('AuthorizeNet', {
  web: () => import('./web').then(m => new m.AuthorizeNetWeb()),
});

export * from './definitions';
export { AuthorizeNet };
