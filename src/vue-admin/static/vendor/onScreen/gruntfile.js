module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        uglify: {
            options: {
                banner: '/*\n* <%= pkg.name %> <%= pkg.version %> \n* Checks if matched elements are inside the viewport. \n* Built on <%= grunt.template.today() %> \n*\n* Copyright <%= grunt.template.today("yyyy") %> <%= pkg.author %> and contributors, Licensed under the MIT license:\n* http://www.opensource.org/licenses/mit-license.php\n*\n* You can find a list of contributors at:\n* https://github.com/silvestreh/onScreen/graphs/contributors\n*/\n\n',
                sourceMap: true,
                compress: {
                    drop_console: true
                }
            },
            js: {
                files: {
                    'jquery.onscreen.min.js': ['jquery.onscreen.js']
                }
            }
        },
        watch: {
            files: ['jquery.onscreen.js'],
            tasks: ['uglify']
        }
    });
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.registerTask('default', ['watch']);
};