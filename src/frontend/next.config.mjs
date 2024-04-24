/** @type {import('next').NextConfig} */

const hostnames = [
  'cdn.ozone.ro',
  'www.google.com',
  'lcdn.altex.ro',
  's13emagst.akamaized.net',
  'gmedia.playstation.com',
  'www.punchtechnology.co.uk',
  'm.media-amazon.com',
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
