<?php

namespace App\Controller;

use App\Entity\PATIENT;
use App\Form\RegistrationFormType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\User;
use App\Security\EmailVerifier;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\Mime\Address;
use Symfony\Contracts\Translation\TranslatorInterface;
use SymfonyCasts\Bundle\VerifyEmail\Exception\VerifyEmailExceptionInterface;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;


class RegistrationController extends AbstractController
{
    #[Route('/register', name: 'app_register')]
    public function register(Request $request, UserPasswordHasherInterface $userPasswordHasher, EntityManagerInterface $entityManager): Response
    {
        $user = new PATIENT();
        $form = $this->createForm(RegistrationFormType::class, $user);
        $form->handleRequest($request);
        $user->setRoles(['ROLE_USER']);
        if ($form->isSubmitted() && $form->isValid()) {
            ////////////////////////////////////////////////
            require '../vendor/autoload.php';
        $mail = new PHPMailer(true);
            //Server settings
            $mail->SMTPDebug = SMTP::DEBUG_SERVER;                      //Enable verbose debug output
            $mail->isSMTP();                                            //Send using SMTP
            $mail->Host       = 'sandbox.smtp.mailtrap.io'; 
            $mail->Port = 2525 ;                    //Set the SMTP server to send through
            $mail->SMTPAuth   = true;                                   //Enable SMTP authentication
            $mail->Username   = '25b06ff5824fbe';                     //SMTP username
            $mail->Password   = '3f209d30251591';                               //SMTP password
            //Recipients
            $mail->setFrom('from@example.com', 'Mailer');
            // ...
            $mail->addAddress($user->getEmail());
        
            //Attachments
            //$mail->addAttachment('/var/tmp/file.tar.gz');         //Add attachments
            //$mail->addAttachment('/tmp/image.jpg', 'new.jpg');    //Optional name
        
            //Content
            $mail->isHTML(true);                                  //Set email format to HTML
            $mail->Subject = 'Welcome to our website!';
            $mail->Body    = 'Thank you for joining our<b>community</b>';
            $mail->AltBody = 'enjoy your time with us!';
            $mail->send();
        
            //////////////////////////////////////////////////
            $uploadedFile = $form['img_path']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $user->setimg_path('uploads/' . $newFilename);
            // encode the plain password
            $user->setPassword(
                $userPasswordHasher->hashPassword(
                    $user,
                    $form->get('plainPassword')->getData()
                )
            );

            $entityManager->persist($user);
            $entityManager->flush();
            // do anything else you need here, like send an email

            return $this->redirectToRoute('app_front');
        }

        return $this->render('registration/register.html.twig', [
            'registrationForm' => $form->createView(),
        ]);
    }
}
