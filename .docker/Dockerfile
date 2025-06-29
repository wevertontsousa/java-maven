FROM amazoncorretto:21-alpine3.21

ARG SYSTEM_USERNAME
ARG SYSTEM_USER_ID
ARG SYSTEM_WORKDIR=/app

RUN apk update && apk upgrade \
  && apk add --no-cache bash git openssh-client starship \
  && rm -rf /var/cache/apk/* \
  && addgroup -g ${SYSTEM_USER_ID} ${SYSTEM_USERNAME} \
  && adduser -D -G ${SYSTEM_USERNAME} -u ${SYSTEM_USER_ID} -s /bin/bash ${SYSTEM_USERNAME} \
  && mkdir -p ${SYSTEM_WORKDIR} \
  && echo 'eval "$(starship init bash)"' >> /home/${SYSTEM_USERNAME}/.bashrc

USER ${SYSTEM_USERNAME}

WORKDIR ${SYSTEM_WORKDIR}
