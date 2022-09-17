import { WebPlugin } from '@capacitor/core';

import type { AuthorizeNetPlugin } from './definitions';

export class AuthorizeNetWeb extends WebPlugin implements AuthorizeNetPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
