export interface EntityException {
  message: string;
  details?: string;
  fieldName?: string;
}

export class ServerResponse<T> {
  ok: boolean = false;
  status: number;
  body: T | EntityException | string;

  constructor(ok: boolean, status: number, body: any) {
    this.ok = ok;
    this.status = status;
    this.body = body;
  }
}
