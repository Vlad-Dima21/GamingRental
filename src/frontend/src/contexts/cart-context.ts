import { createContext, useState } from 'react';

export interface CartItem {
  deviceId?: number;
  gameCopyId?: number;
}

export interface UserContext {
  email: string | undefined;
  cart: CartItem[];
  setUserCart: (cart: CartItem[]) => void;
}

export const CartContext = createContext<UserContext>({
  email: '',
  cart: [],
  setUserCart: () => {},
});

const CART_KEY = 'cart';

export function getCartFromLocalStorage(email: string): CartItem[] {
  const stringifiedCart = localStorage.getItem(CART_KEY);
  if (stringifiedCart) {
    try {
      const entireCart: { [email: string]: CartItem[] } =
        JSON.parse(stringifiedCart);
      if (entireCart?.[email]) {
        return entireCart[email];
      }
    } catch (e: any) {
      console.error(
        `Failed to parse cart from local storage for user ${email}: ${e.message}`
      );
    }
  }
  return [];
}

export function setCartToLocalStorage(email: string, cart: CartItem[]) {
  const stringifiedCart = localStorage.getItem(CART_KEY);
  try {
    const entireCart: { [email: string]: CartItem[] } = stringifiedCart
      ? JSON.parse(stringifiedCart)
      : {};
    entireCart[email] = cart;
    localStorage.setItem(CART_KEY, JSON.stringify(entireCart));
  } catch (e: any) {
    console.error(
      `Failed to save cart to local storage for user ${email}: ${e.message}`
    );
  }
}
