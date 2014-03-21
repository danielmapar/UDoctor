import org.codehaus.groovy.grails.plugins.codecs.SHA256Codec

eventPrepareStaticResource = { info ->

    println "Hashing resources to unique names..."
    
    def f = info.newFile
    def h = SHA256Codec.encode(f.bytes)
    def name = f.name
    def target = new File(f.parentFile, h+'.'+name[name.lastIndexOf('.')+1..-1])
    if (!target.exists()) {
        println "Renaming $f to $target"        
        // Rename or copy here?
        ant.move(file:f, tofile:target, overwrite:false)
    } else {
        println "Skipping rename of $f as hashed file exists"
    }

    // Update mapping entry in manifest
    info.newFile = target
}