'use client';

import React, { forwardRef } from 'react';
import { Button } from '@/components/ui/button';
import { CalendarIcon } from 'lucide-react';

interface DateInputProps {
  value?: string;
  onClick?: () => void;
  disabled?: boolean;
  onChange?: (date: Date | null) => void;
}

export const DateInput = forwardRef<HTMLButtonElement, DateInputProps>(
  ({ value, onClick, disabled }, ref) => (
    <Button
      variant="outline"
      onClick={onClick}
      ref={ref}
      disabled={disabled}
      className="w-full justify-start text-left font-normal"
      type="button"
    >
      <CalendarIcon className="mr-2 h-4 w-4" />
      {value || 'Selecciona un mes y año'}
    </Button>
  )
);

DateInput.displayName = 'DateInput';
