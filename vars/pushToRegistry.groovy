def call(def image, def registryCredentials, def dockerRegistry) {
    docker.withRegistry( dockerRegistry, registryCredentials) {
        image.push()
        image.push('latest')
    }
}
