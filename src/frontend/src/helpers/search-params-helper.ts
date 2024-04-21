import { ReadonlyURLSearchParams } from 'next/navigation';

export default function strippedUrlSearchParams(
  formData: FormData
): ReadonlyURLSearchParams {
  const formParams = Object.fromEntries(formData.entries());
  return new ReadonlyURLSearchParams(
    Object.fromEntries(
      Object.entries(formParams).filter(([_, v]) => !!v)
    ) as Record<string, string>
  );
}
