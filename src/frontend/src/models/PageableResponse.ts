export default interface PageableResponse<T> {
  items: T[];
  totalPages: number;
}
