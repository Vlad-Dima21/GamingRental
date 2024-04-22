import { Metadata } from 'next';
import CartPageContent from './CartPageContent';
import { getSession, logout } from '@/helpers/auth';
import { redirect } from 'next/navigation';
import { CartItem } from '@/contexts/cart-context';
import { post } from '@/helpers/api-helpers';
import { EntityException } from '@/helpers/server-response';

export const metadata: Metadata = {
  title: 'Cart',
};

export interface ActionResult {
  success?: boolean;
  error?: EntityException;
}

export default async function CartPage() {
  const session = await getSession();
  if (!session) {
    redirect('/login');
  }

  async function rentalAction(
    cartItem: CartItem
  ): Promise<ActionResult | undefined> {
    'use server';

    const response = await post('/rentals/create', {
      deviceUnitId: cartItem.deviceUnitId,
      gameCopiesId: cartItem.gameCopiesIds,
      numberOfDays: cartItem.numberOfDays,
    });
    if (!response.ok) {
      if (response.status === 401) {
        logout();
        redirect('/login');
      }
      return {
        success: false,
        error: (await response.json()) as EntityException,
      };
    }
    return { success: true };
  }

  return <CartPageContent rentalAction={rentalAction} />;
}
