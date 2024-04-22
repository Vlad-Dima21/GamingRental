'use client';

import {
  CartContext,
  CartItem,
  getCartFromLocalStorage,
  setCartToLocalStorage,
} from '@/contexts/cart-context';
import { Session } from '@/helpers/auth';
import { useEffect, useState } from 'react';

export default function CartProvider({
  session,
  children,
}: {
  session: Session | null;
  children: React.ReactNode;
}) {
  const sessionEmail = session?.sub;
  const [cart, setCart] = useState<CartItem | undefined>(
    getCartFromLocalStorage(sessionEmail ?? '')
  );

  const setUserCart = (cartItem: CartItem | undefined) => {
    setCart(cartItem);
    setCartToLocalStorage(sessionEmail ?? '', cartItem);
  };

  useEffect(() => {
    setCart(getCartFromLocalStorage(sessionEmail ?? ''));
  }, [sessionEmail]);

  return (
    <CartContext.Provider value={{ email: sessionEmail, cart, setUserCart }}>
      {children}
    </CartContext.Provider>
  );
}
