import webapp2
import jinja2
import os
from google.appengine.ext import db
from gcm import GCM


jinja_environment = jinja2.Environment(
                                       loader=jinja2.FileSystemLoader(os.path.join(os.path.dirname(__file__), 'template')),
                                       autoescape=True,
                                       extensions=['jinja2.ext.autoescape'])
class RegisterDB(db.Model):
    RegId = db.StringProperty(multiline=False)
    
class Home(webapp2.RequestHandler):
    def get(self):
        template = jinja_environment.get_template("index.html")
        self.response.write(template.render())
    def post(self):
        type = self.request.get('type')
        if type == '2':
            sendPush('', '', type)
        else:
            message = self.request.get('body')
            subject = self.request.get('subject')
            sendPush(message, subject, type)
            self.redirect('/')
            
def sendPush(message, subject, type):
    gcm = GCM('AIzaSyDAmrsJsH1jwJHWZvJEKM7xov7Vm3MPDYw')
    recipient = list()
    reg_ids = RegisterDB.all()
    data = {'message': message, 'subject': subject, 'key':type}
    for ids in reg_ids:
        recipient.append(str(ids.RegId))
    gcm.json_request(registration_ids=recipient, data=data)
    
class Register(webapp2.RequestHandler):
    def post(self):
        id = self.request.get('regID')
        regId = RegisterDB(
                           RegId=id
                           )
        regId.put()
        self.response.write('200')

app = webapp2.WSGIApplication([('/', Home),
                               ('/register', Register)                               
                               ], debug=True)

        
