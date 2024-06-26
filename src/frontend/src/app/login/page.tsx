import Form from '@/components/Form';
import { getSession, login } from '@/helpers/auth';
import { Metadata } from 'next';
import { redirect } from 'next/navigation';

export const metadata: Metadata = {
  title: 'Login',
};

export default async function Login() {
  const session = await getSession();
  if (session !== null) {
    redirect('/');
  }
  return (
    <Form
      action={login}
      formTitle='Login'
      inputs={[
        {
          id: 'userEmail',
          name: 'userEmail',
          label: 'Email',
          type: 'email',
          placeholder: 'e.g. john@mail.com',
        },
        {
          id: 'userPassword',
          name: 'userPassword',
          label: 'Password',
          type: 'password',
          placeholder: 'e.g. password1234',
        },
      ]}
      buttonText='Login'
    />
  );
}
