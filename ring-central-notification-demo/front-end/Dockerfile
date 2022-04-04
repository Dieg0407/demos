FROM node:16-alpine as build-stage

WORKDIR /tmp

COPY public ./public
COPY src ./src
COPY package*.json ./
COPY tsconfig.json ./

RUN npm install

RUN npm run build

FROM nginx:1.21.6 as runtime

RUN rm -rf /usr/share/nginx/html

COPY --from=build-stage /tmp/build /usr/share/nginx/html

COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 8080