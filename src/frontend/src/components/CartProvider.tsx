'use client';

import {
  CartContext,
  CartItem,
  getCartFromLocalStorage,
  setCartToLocalStorage,
} from '@/contexts/cart-context';
import { Session } from '@/helpers/auth';
import { useState } from 'react';

export default function CartProvider({
  session,
  children,
}: {
  session: Session | null;
  children: React.ReactNode;
}) {
  const sessionEmail = session?.sub;
  const [cart, setCart] = useState<CartItem[]>(
    getCartFromLocalStorage(sessionEmail ?? '')
  );

  const setUserCart = (cartItems: CartItem[]) => setCart(cartItems);
  return (
    <CartContext.Provider value={{ email: sessionEmail, cart, setUserCart }}>
      {children}
    </CartContext.Provider>
  );
}
