export interface AuthorizeNetPlugin {
  getCardToken(options: CardTokenOptions): Promise<any>;
}

export interface CardTokenOptions {
  clientKey: string,
  loginID: string,
  name: string,
  number: string,
  month: string,
  year: string,
  code: string
};