import Form from '@/components/Form';
import { getSession, register } from '@/helpers/auth';
import { Metadata } from 'next';
import { redirect } from 'next/navigation';

export const metadata: Metadata = {
  title: 'Register',
};

export default async function Register() {
  const session = await getSession();
  if (session !== null) {
    redirect('/');
  }
  return (
    <Form
      action={register}
      formTitle='Register'
      inputs={[
        {
          id: 'userEmail',
          name: 'userEmail',
          label: 'Email',
          type: 'email',
          placeholder: 'e.g. john@mail.com',
          required: true,
        },
        {
          id: 'userName',
          name: 'userName',
          label: 'Name',
          type: 'text',
          placeholder: 'e.g. John Doe',
          required: true,
        },
        {
          id: 'userPhone',
          name: 'userPhone',
          label: 'Phone number',
          type: 'tel',
          placeholder: 'e.g. 0743 817 812',
          required: true,
        },
        {
          id: 'userPassword',
          name: 'userPassword',
          label: 'Password',
          type: 'password',
          placeholder: 'e.g. password1234',
          required: true,
        },
      ]}
      buttonText='Register'
    />
  );
}
