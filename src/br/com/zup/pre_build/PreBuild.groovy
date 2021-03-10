package br.com.zup.pre_build

class PreBuild {
    def call (jenkins) {
        jenkins.podTemplate(
            containers: [
                jenkins.containerTemplate(
                    name: 'flutter', 
                    image: 'flutter', 
                    ttyEnabled: true, 
                    command: 'cat'
                )
            ],
            yamlMergeStrategy: jenkins.merge(),
            workspaceVolume: jenkins.persistentVolumeClaimWorkspaceVolume(
                claimName: "pvc-${jenkins.env.JENKINS_AGENT_NAME}",
                readOnly: false
            )
        ) {
            jenkins.node(jenkins.POD_LABEL){
                jenkins.container('flutter'){
                    try{
                        jenkins.sh label: "Pre-Build flutter", 
                                script: "flutter doctor -v"
                    }
                    catch(Exception e){
                        jenkins.unstable("AN error occured during build step. Please, verify the logs.")
                    }
                }
            }
        }
    }
}