import { WebPlugin } from '@capacitor/core';

import type { AuthorizeNetPlugin, CardTokenOptions } from './definitions';

export class AuthorizeNetWeb extends WebPlugin implements AuthorizeNetPlugin {
  async getCardToken(options: CardTokenOptions): Promise<any> {
    return new Promise((resolve) => {
      try {
        const win: any = window;

        if (win === undefined || !win?.Accept) {
          return resolve({
            response: {
              messages: {
                resultCode: 'Error',
                message: [{
                  text: 'Authorize.net client not initialized',
                }]
              }
            }
          });
        }

        const authData: any = {
          clientKey: options?.clientKey,
          apiLoginID: options?.loginID,
        };

        const cardData: any = {
          cardNumber: options?.number,
          month: options?.month,
          year: options?.year,
          cardCode: options?.code,
          fullName: options?.name,
        };

        const payload = {
          authData: authData,
          cardData: cardData,
        };

        return win?.Accept?.dispatchData(payload, resolve);
      } catch (e: any) {
        return resolve({
          response: {
            messages: {
              resultCode: 'Error',
              message: [{
                text: e ?? 'Unknown error occurred. Please try again.',
              }]
            }
          }
        });
      }
    })
  }
}
