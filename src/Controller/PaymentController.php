<?php

namespace App\Controller;

use Symfony\Component\HttpFoundation\Request; // Ajout de l'importation
use Stripe\Checkout\Session;
use Stripe\Stripe;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;

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
        return $this->render('payment/success.html.twig', []);
    }

    #[Route('/cancel-url', name: 'cancel_url')]
    public function cancelUrl(): Response
    {
        return $this->render('payment/cancel.html.twig', []);
    }
}