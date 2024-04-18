import { getSession } from './auth';

const apiUrl = process.env.BACKEND_URL;

export default ['get', 'post', 'patch'].reduce(
  (
    acc: { [key: string]: (url: string, data?: object) => Promise<Response> },
    method: string
  ) => {
    acc[method] = async (url: string, data?: object): Promise<Response> => {
      const session = await getSession();
      const response = await fetch(`${apiUrl}${url}`, {
        method: method.toUpperCase(),
        headers: {
          'Content-Type': 'application/json',
          ...(session && { Authorization: `Bearer ${session}` }),
        },
        ...(data && { body: JSON.stringify(data) }),
      });
      return response;
    };
    return acc;
  },
  {} as { [key: string]: (url: string, data?: object) => Promise<Response> } // Provide initial value and fix the type
);
