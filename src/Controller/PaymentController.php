<?php
namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request; 
use Symfony\Component\Routing\Generator\UrlGeneratorInterface; // Ajout de l'importation pour UrlGeneratorInterface
use Stripe\Checkout\Session;
use Stripe\Stripe;
use Twilio\Rest\Client;

class PaymentController extends AbstractController
{
    #[Route('/payment', name: 'payment')]
    public function index(): Response
    {
        return $this->render('payment/index.html.twig', [
            'controller_name' => 'PaymentController',
        ]);
    }

    #[Route('/checkout', name: 'checkout')]
    public function checkout(Request $request, $stripeSK): Response
    {
        // Récupérez les détails du produit à partir de la requête
        $productId = $request->query->get('productId');
        $nom = $request->query->get('nom');
        $prix = $request->query->get('prix');
    
        // Initialisez le processus de paiement avec Stripe
        Stripe::setApiKey($stripeSK);
    
        $session = Session::create([
            'payment_method_types' => ['card'],
            'line_items'           => [
                [
                    'price_data' => [
                        'currency'     => 'usd',
                        'product_data' => [
                            'name' => $nom,
                        ],
                        'unit_amount'  => $prix * 100, // Convertir le prix en cents
                    ],
                    'quantity'   => 1,
                ]
            ],
            'mode'                 => 'payment',
            'success_url'          => $this->generateUrl('success_url', [], UrlGeneratorInterface::ABSOLUTE_URL),
            'cancel_url'           => $this->generateUrl('cancel_url', [], UrlGeneratorInterface::ABSOLUTE_URL),
        ]);
    
        // Redirigez l'utilisateur vers la page de paiement Stripe
        return $this->redirect($session->url, 303);
    }

    #[Route('/success-url', name: 'success_url')]
    public function successUrl(): Response
    {
        // Appel de la méthode pour envoyer le SMS après un paiement réussi
        $this->sendPaymentNotification();
        
        return $this->render('payment/success.html.twig', []);
    }

    #[Route('/cancel-url', name: 'cancel_url')]
    public function cancelUrl(): Response
    {
        return $this->render('payment/cancel.html.twig', []);
    }

    #[Route('/payment-notification', name: 'payment_notification')]
    public function sendPaymentNotification(): Response
    {
        // Configurez les identifiants Twilio
        $twilioAccountSid = 'ACc978b03fb24b1c53243c4ac9e321c9e4';
        $twilioAuthToken = '7aa1ba23e284f9213cb9c18c668506f2';
        $twilioPhoneNumber = '+18155678037';

        // Initialisez le client Twilio
        $twilioClient = new Client($twilioAccountSid, $twilioAuthToken);

        // Envoie du SMS
        $message = $twilioClient->messages->create(
            '+21629310624', // Numéro de téléphone de destination
            [
                'from' => $twilioPhoneNumber, // Le numéro Twilio qui enverra le SMS
                'body' => 'Votre paiement a été effectué avec succès.' // Le corps du message SMS
            ]
        );

        // Retournez une réponse HTTP OK
        return new Response('Notification SMS envoyée avec succès.');
    }
}