'use server';

import { cookies } from 'next/headers';
import { jwtDecode } from 'jwt-decode';

type FormState = { error?: string; success?: string } | undefined;

export const login = async (
  prevState: FormState,
  formData: FormData
): Promise<FormState> => {
  const userEmail = formData.get('email') as string;
  const userPassword = formData.get('password') as string;

  const response = await fetch(`${process.env.BACKEND_URL}/api/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ userEmail, userPassword }),
  });

  if (response.ok) {
    const { token }: { token: string; userEmail: string } =
      await response.json();
    const decodedToken = jwtDecode(token);
    if (decodedToken.exp) {
      cookies().set('session', token, {
        httpOnly: true,
        expires: new Date(decodedToken.exp * 1000),
      });
      return { success: 'Login successful' };
    }
  } else {
    const { details }: { message: string; details: string } =
      await response.json();
    return { error: details };
  }
};
