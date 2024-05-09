'use client';

import { Button } from './ui/button';
import { Select } from './ui/select';
import { cn } from '@/lib/utils';

interface SelectLinkProps {
  values: { text: string; value: string }[];
  defaultValue?: string;
  selectName: string;
  placeholder: string;
  className?: string;
}

export default function SelectFilter({
  values,
  placeholder,
  selectName,
  defaultValue,
  className,
}: SelectLinkProps) {
  const clearSelectionText = !!defaultValue ? 'Clear selection' : placeholder;
  return (
    <Button
      asChild
      variant={'outline'}
      className={cn('appearance-none', className)}
    >
      <select
        name={selectName}
        onChange={(event) => event.target.form?.requestSubmit()}
      >
        <option selected={!defaultValue} value={''}>
          {clearSelectionText}
        </option>
        {values.map(({ text, value }) => (
          <option key={value} value={value} selected={defaultValue == value}>
            {text}
          </option>
        ))}
      </select>
    </Button>
  );
}
