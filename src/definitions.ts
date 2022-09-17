export interface AuthorizeNetPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
