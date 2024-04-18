/** @type {import('next').NextConfig} */

const hostnames = [
  'cdn.ozone.ro',
  'www.google.com',
  'lcdn.altex.ro',
  's13emagst.akamaized.net',
];

const nextConfig = {
  images: {
    remotePatterns: hostnames.map((hn) => ({
      protocol: 'https',
      hostname: hn,
    })),
  },
};

export default nextConfig;
