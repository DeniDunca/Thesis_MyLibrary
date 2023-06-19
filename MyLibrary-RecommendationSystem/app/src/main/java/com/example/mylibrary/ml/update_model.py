import firebase_admin
from firebase_admin import ml
from firebase_admin import credentials

firebase_admin.initialize_app(
    credentials.Certificate('thesis-bd8c8-firebase-adminsdk-fqj6e-e1d094b473.json'),
    name = 'app10',
    options={
        'storageBucket': 'thesis-bd8c8.appspot.com',
    })
model = ml.get_model("21841555")
source = ml.TFLiteGCSModelSource.from_tflite_model_file('modified_multi_model.tflite')
model.mode_format = ml.TFLiteFormat(model_source=source)
model.display_name = "my_model"
updated_model = ml.update_model(model)
ml.publish_model(updated_model.model_id)