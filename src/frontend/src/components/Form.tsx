'use client';

import { FormState } from '@/helpers/auth';
import { InputHTMLAttributes, useEffect, useState } from 'react';
import { useFormState } from 'react-dom';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

interface FormInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  placeholder?: string;
}

interface FormProps {
  action: (prevState: FormState, formData: FormData) => Promise<FormState>;
  formTitle: string;
  inputs: Array<FormInputProps>;
  buttonText: string;
}

export default function Form({
  action,
  formTitle,
  inputs,
  buttonText,
}: FormProps): JSX.Element {
  const [formState, formAction] = useFormState(action, undefined);
  const [errorDetails, setErrorDetails] = useState<string | null>(null);

  useEffect(() => {
    if (formState?.error) {
      setErrorDetails(formState.error.details);
      setTimeout(() => {
        setErrorDetails(null);
      }, 5000);
    }
  }, [formState]);

  return (
    <div className='p-16 max-w-5xl md:min-w-[700px] m-auto'>
      <div className='p-16 max-w-5xl md:min-w-[700px] m-auto'>
        <Card>
          <CardHeader>
            <CardTitle>{formTitle}</CardTitle>
          </CardHeader>
          <form action={formAction}>
            <CardContent className='space-y-2'>
              {inputs.map((input) => (
                <div key={input.id}>
                  <Label htmlFor={input.id}>{input.label}</Label>
                  <Input
                    {...input}
                    className={cn(
                      input.className,
                      formState?.error?.fieldName === input.name
                        ? 'border-red-600 !ring-red-600'
                        : ''
                    )}
                  />
                </div>
              ))}
            </CardContent>
            <CardFooter className='flex justify-between gap-2'>
              <Button>{buttonText}</Button>
              {!!formState?.success && (
                <span className='text-green-400'>{formState.success}</span>
              )}
              {!!formState?.error && (
                <span className='text-red-500'>{errorDetails}</span>
              )}
            </CardFooter>
          </form>
        </Card>
      </div>
    </div>
  );
}
